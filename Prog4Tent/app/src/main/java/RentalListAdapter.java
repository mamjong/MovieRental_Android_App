import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.mark.prog4tent.R;
import com.example.mark.prog4tent.domain.Rental;

import java.util.ArrayList;

/**
 * Created by Mika Krooswijk on 15-6-2017.
 */

public class RentalListAdapter extends ArrayAdapter<Rental> {
    public RentalListAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Rental> list) {
        super(context, resource);
    }

    public View getView(int position, View convertview, ViewGroup parent) {
        Rental rental = getItem(position);

        if (convertview == null) {
            convertview = LayoutInflater.from(getContext()).inflate(R.layout.rental_lv_item, parent, false);
        }

        convertview.



        return convertview;
    }
}
