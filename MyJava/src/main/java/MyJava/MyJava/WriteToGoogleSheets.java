package MyJava.MyJava;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import examplePackage.MyAPI;

public class WriteToGoogleSheets {
	public static void main(String[] args) {
        try {
        	MyAPI.hello();
        	
        	int i = MyAPI.return50();
        	System.out.println(i);
        	
        	i = MyAPI.timestwo(i);
        	System.out.println(i);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
