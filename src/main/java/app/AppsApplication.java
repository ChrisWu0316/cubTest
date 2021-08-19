package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"app.service", "app.dao", "app.bean", "app.controller"})
public class AppsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppsApplication.class, args);
		
//		ConfigurableApplicationContext context = SpringApplication.run(AppsApplication.class, args);
//		CurrentPriceService service = context.getBean(CurrentPriceService.class);
//		
//		List<CurrentPriceBean> cpList = service.findAll();
//		
//		System.out.println(cpList.size());
	}

}
