package dev.perf.automation.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;

public class WiremockConfig {

    private static WireMockServer wireMockServer;

    public WiremockConfig() {
        wireMockServer = new WireMockServer(
            WireMockConfiguration.options().asynchronousResponseEnabled( true )
                .port( 5678 )
                .extensions( new ResponseTemplateTransformer( true ) )
                .asynchronousResponseThreads( 50 )
                .containerThreads( 50 )
                .maxRequestJournalEntries( 1000 )
                .withRootDirectory( "src/main/resources" )
        );
        wireMockServer.start();
        WireMock.configureFor( wireMockServer.port() );
        new WireMock( wireMockServer );
    }

    public static WiremockConfig init() {
        return new WiremockConfig();
    }

    public static void shutdown() {
        if( wireMockServer != null ) {
            wireMockServer.stop();
        }
    }

    public boolean isServerRunning() {
        boolean isServerRunning = false;
        if( wireMockServer != null ) {
            isServerRunning = wireMockServer.isRunning();
        }
        return isServerRunning;
    }
}
