package com.symverse.scheduletask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.symverse.sct20.transaction.service.Sct20Factory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SCT20ContractRegisterCronJob {
	
	static final Logger logger = LoggerFactory.getLogger(SCT20ContractRegisterCronJob.class);
	

	
    
	@Autowired private Sct20Factory sct20Factory;
    
  //  @Autowired
  //  public JobBuilderFactory jobBuilderFactory;
  //  
  //  
  //  @Autowired
  //  public StepBuilderFactory stepBuilderFactory;
  //  
  //  
  //  @Autowired
  //  private SimpleJobLauncher jobLauncher;
	
	
//  	*           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
// 초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7) 
//   @Scheduled(cron = "0 * 9 * * ?") 
   // @Scheduled(cron = "0/30 * * * * *")  
   public void cronJobSch() throws Exception {
	   	String amountValue = "1";
	   	
	   	// 0x00028530e81e13060002 재현
	   	// 0x00026b315d4668310002 경훈 
	   	// 0x0002e43f6ff018010002 팀장

		
		// String address = GetKeyStoreJson.getKeyStoreValue("address");
		String toAddreess2 = "0x00026b315d4668310002"; // 재현 -> 김경훈
		String KEYSTORE_FILENAME2 = "keystore.json";
		String resultHashValue2 = sct20Factory.sendRawTransaction(KEYSTORE_FILENAME2, toAddreess2, amountValue);
		System.out.println("재현  to 경훈 :"+ resultHashValue2);
		
		
		// String address = GetKeyStoreJson.getKeyStoreValue("address");
		String toAddreess3 = "0x0002e43f6ff018010002"; //김경훈 - > 팀장님
		String KEYSTORE_FILENAME3 = "kkh-leader.json";
		String resultHashValue4 = sct20Factory.sendRawTransaction(KEYSTORE_FILENAME3, toAddreess3, amountValue);
		System.out.println("경훈 to GD :"+ resultHashValue4);
		

	   	// String address = GetKeyStoreJson.getKeyStoreValue("address");
	   	String toAddreess = "0x00028530e81e13060002";
	   	String KEYSTORE_FILENAME = "gd-king.json";
	   	String resultHashValue = sct20Factory.sendRawTransaction(KEYSTORE_FILENAME, toAddreess, amountValue);
	   	System.out.println(amountValue);
	   	System.out.println("GD to 재현 :"+ resultHashValue);
		
		
		
		
		
		
		
	   
   }
   
   
   // @Scheduled(initialDelay = 10000 , fixedDelay = 10000)  
   public void cronJobScth() throws Exception {
	   
		System.out.println("resultHashValuetest  :");
		
	   
   }
	
	// @Scheduled(initialDelay = 10000 , fixedDelay = 10000) 
	// public void bJob() throws  Exception {
	// 	System.out.println("batch execute ");
	// 	JobParameters param = new JobParametersBuilder().addString("JobId", String.valueOf(System.currentTimeMillis())).toJobParameters();
	// 	JobExecution excExecution = jobLauncher.run(exec(),param);
	// 	System.out.println(excExecution.getStatus());
	// }	
    // 
	// private Job exec() {
	// 	return jobBuilderFactory.get("[exec job]").incrementer(new RunIdIncrementer()).start(execStep()).build();
	// }
    // 
	// private Step execStep() {
	// 	return stepBuilderFactory.get("exec").tasklet(new TestTask()).build();
	// }
	
	
	
}
