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

public class DialogUtils {

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

    public static void showSuccessDialog(Context context, String message, int iconResId) {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.success_popup, null);
        ImageView successIcon = view.findViewById(R.id.successIcon);
        TextView successMessage = view.findViewById(R.id.successMessage);
        Button successButton = view.findViewById(R.id.successButton);

        // Set the icon if provided
        if (iconResId != 0) {
            successIcon.setImageResource(iconResId);
        } else {
            successIcon.setVisibility(View.GONE); // Hide the icon if no resource is provided
        }

        successMessage.setText(message);
        successButton.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.show();
    }

    public static void showConfirmationDialog(Context context, String title, String message, int iconResId, Runnable onConfirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialogStyle);
        
        // Set custom icon
        if (iconResId != 0) {
            builder.setIcon(iconResId);
        }
        
        // Set title and message
        builder.setTitle(title)
               .setMessage(message);

        // Set custom background for buttons
        builder.setPositiveButton("SÍ", (dialog, which) -> onConfirm.run())
               .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Customize button colors
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        
        positiveButton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        negativeButton.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
    }
}
