package al.oxetech.projeto_final;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação.
 * A anotação {@link SpringBootApplication} habilita a configuração automática
 * do Spring e o scan dos componentes.
 */
@SpringBootApplication
public class ProjetoFinalApplication {

    /**
     * Método que inicializa a aplicação Spring Boot.
     */
    public static void main(String[] args) {
        SpringApplication.run(ProjetoFinalApplication.class, args);
    }

}
