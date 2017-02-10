package br.csi.vinicius.appinternetc;

import br.csi.vinicius.appinternetc.R.array;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SelectedActionsAdapter extends android.widget.BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private SparseArray<String> actions = new SparseArray<String>();

    public SelectedActionsAdapter(Context context) {

        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public Object getItem(int position) {
        return actions.get(actions.keyAt(position));
    }

    @Override
    public long getItemId(int position) {
        return actions.keyAt(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (convertView == null) {
            listItem = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        ((TextView) listItem.findViewById(android.R.id.text1)).setText(getItem(position).toString());

        return listItem;
    }

    public int[] getItemsId() {
        int[] keys = new int[actions.size()];

        for (int i = 0; i < actions.size(); i++) {
            keys[i] = actions.keyAt(i);
        }

        return keys;
    }

    public void addItem(int actionIndex) {

        String[] actionsStrings = context.getResources().getStringArray(array.actions);

        actions.append(actionIndex, actionsStrings[actionIndex]);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        actions.remove(actions.keyAt(position));
        notifyDataSetChanged();
    }

}
