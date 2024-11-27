package es.send;

import android.app.Activity;
import android.net.Uri;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * Concrete implementor utilizando la aplicación de WhatsApp. No funciona en el
 * emulador si no se ha configurado previamente
 */
public class WhatsAppImplementor implements SendImplementor {

   /** actividad desde la cual se abrirá la aplicación de WhatsApp */
   private Activity sourceActivity;

   /**
    * Constructor
    * 
    * @param source actividad desde la cual se abrira la aplicación de Whatsapp
    */
   public WhatsAppImplementor(Activity source) {
      setSourceActivity(source);
   }

   /**
    * Actualiza la actividad desde la cual se abrira la actividad de gestion de
    * correo
    */
   public void setSourceActivity(Activity source) {
      sourceActivity = source;
   }

   /** Recupera la actividad desde la cual se abrira la aplicación de Whatsapp */
   public Activity getSourceActivity() {
      return sourceActivity;
   }

   /**
    * Implementacion del metodo send utilizando la aplicacion de WhatsApp
    * 
    * @param phone   teléfono
    * @param message cuerpo del mensaje
    */
   public void send(String phone, String message) {
      PackageManager pm = getSourceActivity().getPackageManager();
      try {
         pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
         
         // Format the phone number correctly
         String formattedPhone = phone.replace(" ", "").replace("+", "");
         
         // Encode the message to handle special characters
         String encodedMessage = Uri.encode(message);
         
         // Construct the WhatsApp URL
         String url = "https://wa.me/" + formattedPhone + "?text=" + encodedMessage;
         
         Intent intent = new Intent(Intent.ACTION_VIEW);
         intent.setData(Uri.parse(url));
         intent.setPackage("com.whatsapp");
         
         getSourceActivity().startActivity(intent);
      } catch (PackageManager.NameNotFoundException e) {
         Toast.makeText(getSourceActivity(), "WhatsApp not Installed",
               Toast.LENGTH_SHORT).show();
      }
   }
}
