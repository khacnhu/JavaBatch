package com.example.BatchProcessing.config;


import com.example.BatchProcessing.entity.Customer;
import com.example.BatchProcessing.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final CustomerRepository customerRepository;
    private final JobRepository jobRepository;

    @Bean
    public FlatFileItemReader<Customer> itemReader() {

        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setName("customer_csv");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());


        return itemReader;
    }


    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("firstName","lastName","email","gender","contactNo","country");
        tokenizer.setStrict(false);

        BeanWrapperFieldSetMapper<Customer> mapper = new BeanWrapperFieldSetMapper<>();

        mapper.setTargetType(Customer.class);
        lineMapper.setFieldSetMapper(mapper);
        lineMapper.setLineTokenizer(tokenizer);

        return lineMapper;

    }

    @Bean
    public CustomerProcessor customerProcessor() {
        return new CustomerProcessor();
    }


    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }


    @Bean
    public RepositoryItemWriter<Customer> itemWriter() {
        RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepository);
        writer.setMethodName("save");

        return writer;

    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("csv-step", jobRepository)
                .<Customer, Customer>chunk(10, platformTransactionManager)
                .reader(itemReader())
                .processor(customerProcessor())
                .writer(itemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }


    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("csv-job", jobRepository)
                .flow(step(jobRepository, platformTransactionManager))
                .end().build();
    }




}
