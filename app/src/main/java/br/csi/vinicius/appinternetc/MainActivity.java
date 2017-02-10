package br.csi.vinicius.appinternetc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener, OnItemClickListener {

    private NfcHelper nfcHelper;
    private ActionHelper actionHelper;
    private ProgressDialog waitingDialog;
    private Spinner Acoes;
    private ListView listaAcoes;
    private SelectedActionsAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcHelper = new NfcHelper(this);
        actionHelper = new ActionHelper(this);

        listAdapter = new SelectedActionsAdapter(this);

        Acoes = (Spinner) findViewById(R.id.acoes);
        Acoes.setOnItemSelectedListener(this);

        listaAcoes = (ListView) findViewById(R.id.listaAcoes);
        listaAcoes.setAdapter(listAdapter);
        listaAcoes.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcHelper.isNfcEnabledDevice()) {
            nfcHelper.enableForegroundDispatch();
        }
        handleIntent(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (nfcHelper.isNfcEnabledDevice()) {
            nfcHelper.disableForegroundDispatch();
        }

    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
            return;
        }

        listAdapter.addItem(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // no-op
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        listAdapter.removeItem(position);

    }

    private void handleAction(ActionHelper.Actions action) {

        switch (action) {
            case WIFI_ON:
                actionHelper.enableWifi();
                break;
            case WIFI_OFF:
                actionHelper.disableWifi();
                break;
            case BLUETOOTH_ON:
                actionHelper.enableBluetooth();
                break;
            case ENABLE_NETWORK:
                actionHelper.enableNetwork();
                break;
            case DISABLE_NETWORK:
                actionHelper.disableNetwork();
                break;
            case BLUETOOTH_OFF:
                actionHelper.disableBluetooth();
                break;
            case ENABLE_GPS:
                actionHelper.enableGps();
                break;
            case DISABLE_GPS:
                actionHelper.disableGps();
                break;
            case NAO_PERTURBE:
                actionHelper.silencioso();
                break;
            case ATIVAR_SONS:
                actionHelper.normal();
                break;
            case VIBRACAO:
                actionHelper.vibracao();
                break;
            default:
                break;
        }

    }

    private void handleIntent(Intent intent) {

        if (!nfcHelper.isNfcIntent(intent)) {
            return;
        }

        if (waitingDialog != null) {

            Integer count = listAdapter.getCount();

            NdefRecord[] records = new NdefRecord[count];

            for (int i = 0; i < count; i++) {
                records[i] = NdefRecord.createExternal("com.appinternetc", "appinternetctipo", new byte[] { (byte) listAdapter.getItemId(i) });
            }

            if (nfcHelper.writeNdefMessage(intent, new NdefMessage(records))) {
                Toast.makeText(this, R.string.toast_write_success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.toast_write_fail, Toast.LENGTH_LONG).show();
            }

            waitingDialog.dismiss();

        } else {

            NdefMessage ndefMessage = nfcHelper.getNdefMessageFromIntent(intent);

            for (NdefRecord ndefRecord : ndefMessage.getRecords()) {

                int actionId = ndefRecord.getPayload()[0];
                ActionHelper.Actions action = ActionHelper.Actions.values()[actionId];

                handleAction(action);

            }

            intent.removeExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            intent.removeExtra(NfcAdapter.EXTRA_TAG);
        }

    }

    public void botaoGravar(View view) {

        if (listAdapter.getCount() == 0) {
            return;
        }

        displayWaitingDialog();
    }

    void displayWaitingDialog() {
        waitingDialog = new ProgressDialog(this);
        waitingDialog.setMessage(getString(R.string.dialog_waiting_tag));
        waitingDialog.setCancelable(true);
        waitingDialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {

                waitingDialog = null;

            }
        });

        waitingDialog.show();
    }


    public void btAnotacao(View view) {
        Intent intent = new Intent(this, AnotacaoActivity.class);

        startActivity(intent);
    }

}
