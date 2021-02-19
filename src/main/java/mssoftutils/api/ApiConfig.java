package mssoftutils.api;

import DomainModel.ICompte;
import io.javalin.Javalin;
import io.javalin.http.Context;
import msdatabaseutils.ICompteValidator;
import msdatabaseutils.IDAO;
import mssoftutils.api.dto.LoginDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;

public abstract class ApiConfig {

    private Javalin javalin;
    private IDAO<ICompte> compteDao;
    private ICompteValidator iCompteValidator;
    private int port;

    public ApiConfig(IDAO<? extends ICompte> compteDao, ICompteValidator iCompteValidator, int port) {
        this.port = port;
        this.compteDao = (IDAO<ICompte>) compteDao;
        this.iCompteValidator = iCompteValidator;

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

            ICompte compte = this.iCompteValidator.isAccountValid(loginDto.getUsername(), loginDto.getPassword());

            if (compte != null) {
                context.json(compte);

                compte.setDateDerniereConnexion(LocalDate.now());
                compte.setHeureDerniereConnexion(LocalTime.now());

                this.compteDao.updateEntity(compte);
            } else
                context.status(403);
        });
    }
}
