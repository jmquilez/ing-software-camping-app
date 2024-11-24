package es.unizar.eina.T213_camping.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import es.unizar.eina.T213_camping.R;

/**
 * Utilidades para mostrar diálogos en la aplicación.
 * Proporciona métodos para mostrar diferentes tipos de diálogos:
 * - Diálogos de carga
 * - Diálogos de éxito
 * - Diálogos de confirmación
 */
public class DialogUtils {

    /**
     * Muestra un diálogo de carga con un mensaje personalizado.
     * @param context Contexto de la aplicación
     * @param message Mensaje a mostrar
     * @return Dialog creado
     */
    public static Dialog showLoadingDialog(Context context, String message) {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.loading_popup, null);
        TextView loadingMessage = view.findViewById(R.id.loadingMessage);
        loadingMessage.setText(message);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    /**
     * Muestra un diálogo de éxito con un mensaje e icono personalizados.
     * @param context Contexto de la aplicación
     * @param message Mensaje de éxito
     * @param iconResId ID del recurso del icono (0 para no mostrar icono)
     */
    public static void showSuccessDialog(Context context, String message, int iconResId) {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.success_popup, null);
        ImageView successIcon = view.findViewById(R.id.successIcon);
        TextView successMessage = view.findViewById(R.id.successMessage);
        Button successButton = view.findViewById(R.id.successButton);

        if (iconResId != 0) {
            successIcon.setImageResource(iconResId);
        } else {
            successIcon.setVisibility(View.GONE);
        }

        successMessage.setText(message);
        successButton.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * Muestra un diálogo de confirmación personalizado.
     * @param context Contexto de la aplicación
     * @param title Título del diálogo
     * @param message Mensaje del diálogo
     * @param iconResId ID del recurso del icono
     * @param onConfirm Runnable a ejecutar si se confirma
     */
    public static void showConfirmationDialog(Context context, String title, String message, int iconResId, Runnable onConfirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialogStyle);
        
        if (iconResId != 0) {
            builder.setIcon(iconResId);
        }
        
        builder.setTitle(title)
               .setMessage(message);

        builder.setPositiveButton("SÍ", (dialog, which) -> onConfirm.run())
               .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        
        positiveButton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        negativeButton.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
    }

    /**
     * Muestra un diálogo de error con un mensaje.
     * @param context Contexto de la aplicación
     * @param message Mensaje de error
     */
    public static void showErrorDialog(Context context, String message) {
        new AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
            .show();
    }
}
