package Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

public class SecurityConfiguration  {
    
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();

        CorsConfiguration configuration=new CorsConfiguration();
        configuration.setAllowCredentials(false);
        configuration.applyPermitDefaultValues();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("HEAD");
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**",configuration);

        return new CorsFilter(source);
    }
}
