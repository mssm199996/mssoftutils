package mssoftutils.api;

import io.javalin.Javalin;
import io.javalin.http.Context;
import mssoftutils.api.dto.LoginDto;

import java.util.function.Consumer;

public abstract class ApiConfig<C> {

    private Javalin javalin;
    private int port;

    public ApiConfig(int port) {
        this.port = port;

        this.startup();
        this.configure();
    }

    public void startup() {
        this.javalin = Javalin.create().start(this.port);
    }

    public void get(String path, Consumer<Context> consumer) {
        this.javalin.get(path, context -> consumer.accept(context));
    }

    public void post(String path, Consumer<Context> consumer) {
        this.javalin.post(path, context -> consumer.accept(context));
    }

    public void patch(String path, Consumer<Context> consumer) {
        this.javalin.patch(path, context -> consumer.accept(context));
    }

    protected void configure() {
        this.post("/login", context -> {
            LoginDto loginDto = context.bodyAsClass(LoginDto.class);

            C compte = this.login(loginDto);

            if (compte != null) {
                context.json(compte);

                this.afterLogin(compte);
            } else
                context.status(403);
        });
    }

    public abstract C login(LoginDto loginDto);

    public abstract void afterLogin(C compte);
}
