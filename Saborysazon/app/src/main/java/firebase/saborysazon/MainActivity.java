package firebase.saborysazon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import firebase.saborysazon.models.Item_menu;
import firebase.saborysazon.ui.DatePickerFragment;

public class MainActivity extends AppCompatActivity {
    private EditText fecha;
    private EditText nombre;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button btnDisplay;
    private ListView listV;
    List<Item_menu> lista = new ArrayList<Item_menu>();
    ArrayAdapter<Item_menu> array;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fecha = findViewById(R.id.Date);
        nombre= findViewById(R.id.nombre);
        radioSexGroup = (RadioGroup) findViewById(R.id.grupo);
        btnDisplay = (Button) findViewById(R.id.agregar);
        listV=findViewById(R.id.lista_item);
        init_firebase();
        
        
        
        listar_datos();
    }

    private void listar_datos() {
        reference.child("Item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista.clear();
                for (DataSnapshot obj:dataSnapshot.getChildren()){
                    Item_menu it = obj.getValue(Item_menu.class);
                    lista.add(it);
                    array = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,lista);
                    listV.setAdapter(array);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init_firebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Date:
                showDatePickerDialog();
                break;
            case R.id.agregar:
                int selectedid = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = findViewById(selectedid);

                Item_menu item=new Item_menu();
                item.setID(UUID.randomUUID().toString());
                item.setNombre(nombre.getText().toString());
                item.setFecha(fecha.getText().toString());
                item.setTipo(radioSexButton.getText().toString());
                reference.child("Item").child(item.getID()).setValue(item);




                Toast.makeText(getApplicationContext(),"agregado", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    //////////////////////////////////////////////////////////////// Datapiker  ////////////////////////////////////////////////////
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                fecha.setText(selectedDate);
            }
        });

        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }
}
