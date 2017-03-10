package org.mattreyuk.adtech.config;

import static com.google.common.collect.Lists.newArrayList;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
// don't run this when started with production profile
@Profile("!production")
public class SwaggerConfig {
	@Autowired
	private TypeResolver typeResolver;

	@Bean
	public ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("Adtech Service",
				"Service provides best bid ad for a user together with history data and ad click notification",
				"1.0.0", "http://swagger.io", "mattreyuk@gmail.com", "MIT License",
				"http://opensource.org/licenses/MIT");
		return apiInfo;
	}

	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("org.mattreyuk.adtech"))
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/")
				.directModelSubstitute(LocalDate.class, String.class)
				.genericModelSubstitutes(ResponseEntity.class)
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, newArrayList(new ResponseMessageBuilder().code(500)
						.message("Unexpected Error")
						.responseModel(new ModelRef("Error"))
						.build(), new ResponseMessageBuilder().code(400)
						.message("Invalid Data")
						.responseModel(new ModelRef("Error"))
						.build()))
				.globalOperationParameters(newArrayList(new ParameterBuilder().name("User-Agent")
						.defaultValue("Browser-User-Agent")
						.description("User-Agent header")
						.modelRef(new ModelRef("string"))
						.parameterType("header")
						.required(false)
						.build()))
				.apiInfo(apiInfo());
	}

}
