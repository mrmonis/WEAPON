package asgard.weapon.timetable;

import java.util.ArrayList;

import asgard.weapon.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import asgard.weapon.ConditionCodes;

/**
 * 
 * @author Jarrett
 * 
 *         An activity to manage creation of new timetables (TEMPORARY)
 * 
 */
public class TimetableSelectionForm extends Activity implements OnClickListener,
		Handler.Callback {

	private TimetableController mController;

	private Handler mHandler;
	
	private ArrayList<Timetable> mTimetables;
	
	private RelativeLayout mView;
	private boolean delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//setContentView(R.layout.timetable_selection_form);

		delete = false;
		mHandler = new Handler(this);

		mController = TimetableController.getController(this);
		mController.addHandler(mHandler);
		
		mController.getHandler().obtainMessage(ConditionCodes.V_GET_ALL_TIMETABLE).sendToTarget();
	}

	@Override
	public void onClick(View v) {
		Handler handler = mController.getHandler();
		Message message = handler.obtainMessage(ConditionCodes.V_DO_NOTHING,
				this);
		
		int position = v.getId();
		
		Message msg = mController.getHandler().obtainMessage();
		
		if (delete){
			msg.what = ConditionCodes.V_DELETE_SELECTED;
		}
		else{
			msg.what = ConditionCodes.V_TIMETABLE_SELECTED;
		}
		msg.arg1 = position;
		msg.sendToTarget();
		finish();
		//test

	}
	
	public void initialize(){
		//mView = (RelativeLayout) findViewById(R.id.button_container);
		LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		
		for (int i = 0; i < mTimetables.size(); i++){
			
			RelativeLayout rl = (RelativeLayout) li.inflate(
					R.layout.button_form, null);
			
			Button temp = (Button) rl.findViewById(R.id.button);
			TextView name = (TextView) rl.findViewById(R.id.text_view);
			temp.setOnClickListener(this);
			temp.setText(mTimetables.get(i).getName());
			temp.setId(i);
			
			LayoutParams buttonLP = (LayoutParams) temp.getLayoutParams();
			
			if (i != 0) {
				Button aboveButton = (Button) findViewById(i-1);
				buttonLP.addRule(RelativeLayout.BELOW, aboveButton.getId());
				temp.setLayoutParams(buttonLP);

			} else {
				buttonLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				temp.setLayoutParams(buttonLP);
			}

			

			rl.removeAllViews();
			mView.addView(temp);
		}		
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mController.removeHandler(mHandler);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what){
		case ConditionCodes.C_SEND_ALL_TIMETABLE:
			mTimetables = (ArrayList<Timetable>) msg.obj;
			if (msg.arg1 != 0){
				delete = true;
			}
			initialize();
			break;
		}
		return false;
	}
}