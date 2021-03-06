package com.mimi.github.ui.user;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.ViewUtils;
import com.mimi.github.R;
import com.mimi.github.Util.AvatarLoader;
import com.mimi.github.Util.TimeUtils;
import com.mimi.github.Util.TypefaceUtils;
import com.mimi.github.ui.StyledText;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.event.CreatePayload;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.EventRepository;
import org.eclipse.egit.github.core.event.GistPayload;
import org.eclipse.egit.github.core.event.IssueCommentPayload;
import org.eclipse.egit.github.core.event.IssuesPayload;

import static org.eclipse.egit.github.core.event.Event.TYPE_COMMIT_COMMENT;
import static org.eclipse.egit.github.core.event.Event.TYPE_CREATE;
import static org.eclipse.egit.github.core.event.Event.TYPE_DELETE;
import static org.eclipse.egit.github.core.event.Event.TYPE_DOWNLOAD;
import static org.eclipse.egit.github.core.event.Event.TYPE_FOLLOW;
import static org.eclipse.egit.github.core.event.Event.TYPE_FORK;
import static org.eclipse.egit.github.core.event.Event.TYPE_FORK_APPLY;
import static org.eclipse.egit.github.core.event.Event.TYPE_GIST;
import static org.eclipse.egit.github.core.event.Event.TYPE_GOLLUM;
import static org.eclipse.egit.github.core.event.Event.TYPE_ISSUES;
import static org.eclipse.egit.github.core.event.Event.TYPE_ISSUE_COMMENT;
import static org.eclipse.egit.github.core.event.Event.TYPE_MEMBER;
import static org.eclipse.egit.github.core.event.Event.TYPE_PUBLIC;
import static org.eclipse.egit.github.core.event.Event.TYPE_PULL_REQUEST;
import static org.eclipse.egit.github.core.event.Event.TYPE_PULL_REQUEST_REVIEW_COMMENT;
import static org.eclipse.egit.github.core.event.Event.TYPE_PUSH;
import static org.eclipse.egit.github.core.event.Event.TYPE_TEAM_ADD;
import static org.eclipse.egit.github.core.event.Event.TYPE_WATCH;

/**
 * Created by zwb on 15-10-21.
 */
public class NewsListAdapter extends SingleTypeAdapter<Event> {
    private  static  final String TAG = "NewsListAdapter";

    public static boolean isValid(final Event event) {
        if (event == null)
            return false;

        final EventPayload payload = event.getPayload();
        if (payload == null || EventPayload.class.equals(payload.getClass()))
            return false;

        final String type = event.getType();
        if (TextUtils.isEmpty(type))
            return false;

        return TYPE_COMMIT_COMMENT.equals(type) //
                || (TYPE_CREATE.equals(type) //
                && ((CreatePayload) payload).getRefType() != null) //
                || TYPE_DELETE.equals(type) //
                || TYPE_DOWNLOAD.equals(type) //
                || TYPE_FOLLOW.equals(type) //
                || TYPE_FORK.equals(type) //
                || TYPE_FORK_APPLY.equals(type) //
                || (TYPE_GIST.equals(type) //
                && ((GistPayload) payload).getGist() != null) //
                || TYPE_GOLLUM.equals(type) //
                || (TYPE_ISSUE_COMMENT.equals(type) //
                && ((IssueCommentPayload) payload).getIssue() != null) //
                || (TYPE_ISSUES.equals(type) //
                && ((IssuesPayload) payload).getIssue() != null) //
                || TYPE_MEMBER.equals(type) //
                || TYPE_PUBLIC.equals(type) //
                || TYPE_PULL_REQUEST.equals(type) //
                || TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type) //
                || TYPE_PUSH.equals(type) //
                || TYPE_TEAM_ADD.equals(type) //
                || TYPE_WATCH.equals(type);
    }

    private AvatarLoader avatarLoader;

    private boolean showRepoName;

    public NewsListAdapter(LayoutInflater inflater, Event[] events, AvatarLoader
                           avatarLoader){
        super(inflater, R.layout.news_item);

        this.avatarLoader = avatarLoader;
        this.showRepoName = true;
        setItems(events);

    }

    public NewsListAdapter(LayoutInflater inflater, Event[] events, AvatarLoader
            avatarLoader, boolean showRepoName){
        super(inflater, R.layout.news_item);

        this.avatarLoader = avatarLoader;
        this.showRepoName = showRepoName;
        setItems(events);
    }

    public NewsListAdapter(LayoutInflater inflater, AvatarLoader avatarLoader){
        this(inflater, null, avatarLoader);
    }

    @Override
    public long getItemId(final int position) {
        final String id = getItem(position).getId();
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.iv_avatar, R.id.tv_event, R.id.tv_event_details,
                R.id.tv_event_icon, R.id.tv_event_date };
    }

    @Override
    protected View initialize(View view) {
        view = super.initialize(view);

        TypefaceUtils.setOcticons(textView(view, 3));
        return view;
    }

    @Override
    protected void update(int i, Event event) {
        Log.d(TAG, "------update list view --------");
        avatarLoader.bind(imageView(0), event.getActor());

        String type = event.getType();
        Log.d(TAG,"-------- update list type = " + type);

        StyledText main = new StyledText();
        StyledText details = new StyledText();
        String icon = null;

        switch (type){
            case Event.TYPE_COMMIT_COMMENT:
                break;
            case Event.TYPE_CREATE:
                break;
            case Event.TYPE_DELETE:
                break;
            case Event.TYPE_DOWNLOAD:
                break;
            case Event.TYPE_FOLLOW:
                break;
            case Event.TYPE_FORK:
                icon = TypefaceUtils.ICON_REPO_FORKED;
                formatForkRepo(event,main,details);
                break;
            case Event.TYPE_FORK_APPLY:
                break;
            case Event.TYPE_GIST:
                break;
            case Event.TYPE_GOLLUM:
                break;
            case Event.TYPE_ISSUE_COMMENT:
                break;
            case Event.TYPE_MEMBER:
                break;
            case Event.TYPE_ISSUES:
                break;
            case Event.TYPE_PUBLIC:
                break;
            case Event.TYPE_PULL_REQUEST:
                break;
            case Event.TYPE_PULL_REQUEST_REVIEW_COMMENT:
                break;
            case Event.TYPE_TEAM_ADD:
                break;
            case Event.TYPE_WATCH:
                icon = TypefaceUtils.ICON_STAR;
                formatWatch(event,main,details);
                break;
        }

        if(icon != null){
            ViewUtils.setGone(setText(3,icon), false);
        }else{
            setGone(3, true);
        }

        if(main != null){
            setText(1, main);
        }

        if(!TextUtils.isEmpty(details)){
            ViewUtils.setGone(setText(2,details), false);
        }else {
            setGone(2,true);
        }

        setText(4, TimeUtils.getRelativeTime(event.getCreatedAt()));
    }

    private StyledText boldActor(StyledText styledText, final Event event){
        return boldUser(styledText, event.getActor());
    }

    private StyledText boldUser(StyledText styledText, final User user){
        if(styledText != null){
            styledText.bold(user.getLogin());
        }
        return styledText;
    }

    private StyledText boldRepo(StyledText styledText, final Event event){
        if(styledText != null){
            EventRepository repository = event.getRepo();
            styledText.bold(repository.getName());
        }
        return styledText;
    }

    private void formatForkRepo(Event event, StyledText main, StyledText details){
        boldActor(main,event);
        main.append(" forked repository ");

        if(showRepoName){
            boldRepo(main,event);
        }
    }

    private void formatWatch(Event event, StyledText main,
                        StyledText details){
        boldActor(main, event);
        main.append(" starred ");

        if(showRepoName){
            boldRepo(main,event);
        }

    }
}
