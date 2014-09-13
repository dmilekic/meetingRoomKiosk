package tr.com.aliok.meetingroomkiosk.android.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cengalabs.flatui.views.FlatTextView;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import tr.com.aliok.meetingroomkiosk.android.R;
import tr.com.aliok.meetingroomkiosk.android.components.BlinkingTextView;
import tr.com.aliok.meetingroomkiosk.android.components.CountDownTextView;
import tr.com.aliok.meetingroomkiosk.android.model.Event;

/**
 * Fragment for displaying current session tab. It includes detail for current session (or next session)
 * and list of today's upcoming sessions.
 * <p/>
 * Activities that contain this fragment must implement the
 * {@link NowAndTodayOverviewFragment.ActivityContract} interface
 * to handle interaction events.
 * Use the {@link NowAndTodayOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NowAndTodayOverviewFragment extends Fragment implements CountDownTextView.CountDownListener {

    // ------ activity  --------------------------
    private ActivityContract activityContract;

    // ------ component bindings -----------------
    private FlatTextView mCurrentOrNextEventBannerTextView;
    private CountDownTextView mCountDownTextView;
    private BlinkingTextView mOnAirTextView;
    private View mCurrentEventDetailFragment;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment
     */
    public static NowAndTodayOverviewFragment newInstance() {
        return new NowAndTodayOverviewFragment();
    }

    public NowAndTodayOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_now_or_today_overview, container, false);

        // this is the moment to bind components, not before
        bindComponents(view);


        this.mCountDownTextView.setCountDownListener(this);

        doRefreshViewWithDataFromActivity();

        return view;
    }

    private void bindComponents(View view) {
        this.mCurrentOrNextEventBannerTextView = (FlatTextView) view.findViewById(R.id.currentOrNextEventBannerTextView);
        this.mCountDownTextView = (CountDownTextView) view.findViewById(R.id.countDownTextView);
        this.mOnAirTextView = (BlinkingTextView) view.findViewById(R.id.onAirTextView);
        this.mCurrentEventDetailFragment = view.findViewById(R.id.currentEventDetailFragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityContract = (ActivityContract) activity;
            activityContract.setNowAndTodayOverviewFragment(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ActivityContract");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityContract.setNowAndTodayOverviewFragment(null);
        activityContract = null;
    }

    public void dataArrived() {
        doRefreshViewWithDataFromActivity();
    }

    private void doRefreshViewWithDataFromActivity() {
        final Event currentEvent = activityContract.getCurrentEvent();
        final List<Event> upcomingEvents = activityContract.getUpcomingEvents();

        // perhaps the fragment is created but the data request of activity is not done yet!
        // so, check if activity received the data
        if (currentEvent == null) {
            if (CollectionUtils.isEmpty(upcomingEvents)) {
                // TODO: display "no upcoming event"

                // hide event detail fragment for current or next event
                mCurrentEventDetailFragment.setVisibility(View.GONE);

                // update banner text and style
                mCurrentOrNextEventBannerTextView.setText(getResources().getText(R.string.no_session_in_progress));
                mCurrentOrNextEventBannerTextView.setBackgroundColor(getResources().getColor(R.color.snow_primary));
                mCurrentOrNextEventBannerTextView.setTextColor(getResources().getColor(R.color.grass_dark));

                // stop countdown and hide it
                mCountDownTextView.setVisibility(View.INVISIBLE);
                mCountDownTextView.stopCountDown();

                // hide on air blinking text
                mOnAirTextView.setVisibility(View.INVISIBLE);
                mOnAirTextView.stopBlinking();

            } else {
                final Event nextEvent = upcomingEvents.get(0);      // they're already sorted, get the first one
                // TODO: display next event details

                // show event detail fragment
                mCurrentEventDetailFragment.setVisibility(View.VISIBLE);

                // update banner text and style
                mCurrentOrNextEventBannerTextView.setText(getResources().getText(R.string.next_session));
                mCurrentOrNextEventBannerTextView.setBackgroundColor(getResources().getColor(R.color.snow_primary));
                mCurrentOrNextEventBannerTextView.setTextColor(getResources().getColor(R.color.blood_darker));

                // start countdown for start of next event
                mCountDownTextView.setVisibility(View.VISIBLE);
                mCountDownTextView.startCountDown(nextEvent.getEventStart());

                // hide on air blinking text
                mOnAirTextView.setVisibility(View.INVISIBLE);
                mOnAirTextView.stopBlinking();
            }
        } else {
            // TODO display current event details

            // show event detail fragment
            mCurrentEventDetailFragment.setVisibility(View.VISIBLE);

            // update banner text and style
            mCurrentOrNextEventBannerTextView.setText(getResources().getText(R.string.session_in_progress));
            mCurrentOrNextEventBannerTextView.setBackgroundColor(getResources().getColor(R.color.grass_primary));
            mCurrentOrNextEventBannerTextView.setTextColor(getResources().getColor(R.color.grass_darker));

            // start count down for end of current event
            mCountDownTextView.setVisibility(View.VISIBLE);
            mCountDownTextView.startCountDown(currentEvent.getEventEnd());

            // show on air blinking text
            mOnAirTextView.setVisibility(View.VISIBLE);
            mOnAirTextView.startBlinking();
        }

        if (CollectionUtils.isEmpty(upcomingEvents)) {
            // TODO display "no upcoming events"
        } else {
            // TODO display list of upcoming events
        }

        // TODO refresh view when countdown is finished
        // TODO best way : creating a countdown component which notifies parent when countdown == 00:00:00
    }

    @Override
    public void onCountDownFinished(CountDownTextView countDownTextView) {
        activityContract.refresh();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     */
    public interface ActivityContract {
        public void setNowAndTodayOverviewFragment(NowAndTodayOverviewFragment nowAndTodayOverviewFragment);

        void onEventSelected(Event event);

        Event getCurrentEvent();

        List<Event> getUpcomingEvents();

        void refresh();
    }

}
