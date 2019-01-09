package student.assignment.taruc.givemeyourhand;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutFragment extends Fragment {

    TextView about;
    public AboutFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        about = (TextView) view.findViewById(R.id.about_text);
        return view;

    }

    public void onStart() {

        super.onStart();
        String abtStr = "Privacy Policies \n" + "Your Privacy is important to Our App Service. Our Privacy Policy which make our users feel more protected with their account Information." +
                "\n\nChanges to this Privacy Policy\n" +
                "We may update our Privacy Policy from time to time. Thus, you are advise to review this page frequently for any changes. "+
                "These changes are effective  immediately after they are posted on this page.\n\n"+
                "Contact Us\n" + "The developers of this apps: \n" + "1. Chan Wei An \n2. Loke Choon Yen \n" + "Please do not hesitate to contact us if got any suggestions or questions. "+
                "Thanks for the supporting...";
        about.setText(abtStr);
    }

}


