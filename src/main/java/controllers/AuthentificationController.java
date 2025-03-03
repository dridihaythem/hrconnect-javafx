package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

import java.awt.*;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AuthentificationController {
    // 🛠️ Clés reCAPTCHA
    private static final String SITE_KEY = "6LdEsOcqAAAAAME9Ww5HIlrLMMof-rbnA8U51C6y";
    private static final String SECRET_KEY = "6LdEsOcqAAAAAKZ1qtdgmNwmdbGkrJVszLznKEal";
    @FXML
    private WebView recaptchaWebView;
    @FXML
    private Button googleSignInButton;

    // 🛠️ Clés Google Sign-In
    private static final String CLIENT_ID = "598450094933-isjdvk87e0gnhkh06h250efgoe9ud0sq.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob"; // Evite LocalServerReceiver

    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email");

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    @FXML
    void annuler(ActionEvent event) {
        // Close the current window
        Button source = (Button) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        WebEngine webEngine = recaptchaWebView.getEngine();

        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <script src='https://www.google.com/recaptcha/api.js'></script>" +
                "</head>" +
                "<body>" +
                "    <form action='#'>" +
                "        <div class='g-recaptcha' data-sitekey='" + SITE_KEY + "'></div>" +
                "        <button type='submit'>Vérifier</button>" +
                "    </form>" +
                "</body>" +
                "</html>";

        webEngine.loadContent(html, "text/html");
    }

    // ✅ Vérification du reCAPTCHA
    public boolean verifyRecaptcha(String responseToken) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://www.google.com/recaptcha/api/siteverify");
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            StringEntity entity = new StringEntity("secret=" + SECRET_KEY + "&response=" + responseToken);
            post.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(post)) {
                InputStream inputStream = response.getEntity().getContent();
                String result = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
                JSONObject json = new JSONObject(result);
                return json.getBoolean("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Google Sign-In
    @FXML
    void handleGoogleSignIn(ActionEvent event) {
        try {
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES)
                    .setAccessType("offline")
                    .build();

            // 🔹 Ouvrir l'URL de connexion dans le navigateur
            String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
            Desktop.getDesktop().browse(new URI(authorizationUrl));

            // 🔹 Demander à l'utilisateur d'entrer le code
            String code = showInputDialog("Entrez le code Google obtenu:");

            // 🔹 Échanger le code contre un token
            GoogleTokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
            Credential credential = flow.createAndStoreCredential(tokenResponse, "user");

            // 🔹 Récupérer les informations utilisateur
            Oauth2 oauth2 = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("RHCONNECT").build();
            Userinfo userinfo = oauth2.userinfo().get().execute();

            String email = userinfo.getEmail();
            String name = userinfo.getName();

            showAlert("Connexion réussie avec Google", "Bienvenue, " + name + " !");
        } catch (Exception e) {
            showAlert("Erreur de connexion", "Impossible de se connecter via Google");
            e.printStackTrace();
        }
    }

    // ✅ Affichage d'alertes
    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // ✅ Demander un code à l'utilisateur
    private String showInputDialog(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Google Sign-In");
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        return dialog.showAndWait().orElse("");
    }
}
