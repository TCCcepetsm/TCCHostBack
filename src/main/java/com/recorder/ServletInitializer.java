import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // Configuração adicional para deploy em container
        setRegisterErrorPageFilter(false); // Desativa filtro de erro padrão
        return application.sources(RecorderSrcApplication.class)
                .properties("spring.config.name:application,postgresql");
    }
}