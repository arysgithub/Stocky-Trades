module com.company.financeflowapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires io.fair_acc.chartfx;
    requires org.json;

    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires java.net.http;
    requires io.fair_acc.dataset; // need this so your new dependencies can work
    requires org.slf4j;
    requires logback.classic;
    requires logback.core;
    requires org.jetbrains.annotations;

    opens com.company.financeflowapp to javafx.fxml;
    exports com.company.financeflowapp;
    exports com.company.financeflowapp.main;
    opens com.company.financeflowapp.main to javafx.fxml;
    opens com.company.financeflowapp.portfolio to javafx.base;
    //This opens directive allows the com.company.financeflowapp.portfolio package to be accessed by modules that require it, including JavaFX's base module (javafx.base).
    //
    //After making this change, rebuild your project and run it again. The issue should be resolved, and you should no longer encounter the IllegalAccessException related to accessing PortfolioItem from the JavaFX base module.
}