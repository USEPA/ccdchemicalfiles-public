package gov.epa.ccte.api.chemicalfiles.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.List;

@Component
public class SpringDataRestCustomization implements RepositoryRestConfigurer {

    @Qualifier("defaultValidator")
    @Autowired
    private Validator validator;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors)  {

        cors.addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");

//        config.getCorsRegistry().addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");

/*        config.getCorsRegistry().addMapping("/**")
        .allowedOrigins("http://localhost:3000","http://localhost:3003", "http://comptox-int-edge.epa.gov",
                "http://ho.epa.gov:","http://ccte-ccd-stg.epa.gov","http://ccte-ccd-dev.epa.gov","http://ccte-ccd.epa.gov");*/



        // TODO *** I need to watch for this bug (https://jira.spring.io/browse/DATAREST-1405)
        // PUT doesn't work when EntityLookup is configured, like I am doing here.

        // This will allow to use DTXCID instead of database generated Id (By default HETOAS uses Id field for self link)
//        config.withEntityLookup().forRepository(SourceSubstanceRepository.class,
//                SourceSubstance::getSourceSubstanceId,
//                SourceSubstanceRepository::findBySourceSubstanceId);

/*      -- Following are few setting which I am not configuring but putting them here for reference
        config.setBasePath("/sdr-api");
        config.setReturnBodyOnCreate(Boolean.TRUE);
        config.setReturnBodyOnUpdate(Boolean.TRUE);

        // config.exposeIdsFor(Project.class);
        config.useHalAsDefaultJsonMediaType(Boolean.TRUE);
        config.setDefaultMediaType(MediaType.APPLICATION_JSON);

        config.setSortParamName();
        config.setLimitParamName();
        config.setMaxPageSize()
        config.setDefaultPageSize();
 */
    }

    @Override
    public void configureConversionService(ConfigurableConversionService conversionService) {

    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", validator);
    }

    @Override
    public void configureExceptionHandlerExceptionResolver(ExceptionHandlerExceptionResolver exceptionResolver) {

    }

    @Override
    public void configureHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {

    }

    @Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {

    }
}
