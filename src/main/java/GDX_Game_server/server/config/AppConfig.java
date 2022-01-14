package GDX_Game_server.server.config;

import GDX_Game_server.server.ws.GameLoop;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class AppConfig {
    @Bean
    public HeadlessApplication getApplication(GameLoop gameLoop){
        return new HeadlessApplication(gameLoop);
    }
}
