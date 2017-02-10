package br.csi.vinicius.appinternetc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AnotacaoActivity extends AppCompatActivity {

    NfcHelper nfcHelper;
    TextView txtNote;

    Boolean isWritingTag = false;
    ProgressDialog writingProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nfcHelper = new NfcHelper(this);


        setContentView(R.layout.activity_anotacao);

        txtNote = (TextView) findViewById(R.id.txtNote);

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcHelper.isNfcEnabledDevice()) {
            nfcHelper.enableForegroundDispatch();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcHelper.isNfcEnabledDevice()) {
            nfcHelper.disableForegroundDispatch();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (nfcHelper.isNfcIntent(intent)) {
            if (isWritingTag) {

                String text = txtNote.getText().toString();

                NdefMessage ndefMsg = nfcHelper.createTextNdefMessage(text);

                if (nfcHelper.writeNdefMessage(intent, ndefMsg)) {

                    Toast.makeText(this, R.string.toast_write_successful, Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(this, R.string.toast_write_fail, Toast.LENGTH_LONG).show();

                }

                isWritingTag = false;
                writingProgressDialog.dismiss();

            } else {

                // Check Chapter 5 to know how to get tag content

            }
        }

    }

    public void handleIntent(Intent intent) {

        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {

                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    txtNote.setText(sharedText);
                }
            }
        }
    }

    public void onBtWriteTagClick(View view) {
        String text = txtNote.getText().toString();

        if (text.isEmpty()) {
            Toast.makeText(this, R.string.toast_invalid_note, Toast.LENGTH_LONG).show();

            return;
        }

        showWaitDialog();
    }

    private void showWaitDialog() {

        writingProgressDialog = ProgressDialog.show(this, "", getString(R.string.dialog_tap_on_tag), false, true, new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                isWritingTag = false;
            }
        });

        isWritingTag = true;

    }

    public void retornar(View view) {
        Intent intent = new Intent(this,MainActivity.class);

        startActivity(intent);
    }

}
