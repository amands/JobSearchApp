package slinfy.android.jobsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JobSearchActivity extends Activity {
	/** Called when the activity is first created. */

	EditText etKey, etLoc;
	Button btnResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		etKey = (EditText) findViewById(R.id.etKeyword);
		etLoc = (EditText) findViewById(R.id.etLocation);
		btnResult = (Button) findViewById(R.id.btnShowResult);

		btnResult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if ((etKey.getText().toString().length() < 1)
						|| (etLoc.getText().toString().length() < 1)) {
					Toast.makeText(JobSearchActivity.this,
							"Please Fill The Above Text Fields", Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(JobSearchActivity.this,
							JobList.class);
					intent.putExtra("q", etKey.getText().toString());
					intent.putExtra("l", etLoc.getText().toString());
					startActivity(intent);
				}
			}
		});
	}
}