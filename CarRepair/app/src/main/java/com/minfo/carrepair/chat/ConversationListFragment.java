package com.minfo.carrepair.chat;


import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.minfo.carrepair.R;
import com.minfo.carrepair.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationListFragment extends EaseConversationListFragment {

    private TextView errorText;
    private  Utils utils;

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        utils=new Utils(getActivity());
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);

                String username = conversation.conversationId();

                String nickname = "";
                String img="";
                try {

                    nickname = conversation.getLatestMessageFromOthers().getStringAttribute("userNickName");
                    img = conversation.getLatestMessageFromOthers().getStringAttribute("userHeadImage");
                    Log.e("toChatUsername", nickname + "  " + img);

//                    nickname = conversation.getLatestMessageFromOthers().getStringAttribute("userNickName");
//
//                    nickname = conversation.getLatestMessageFromOthers().getStringAttribute("userNickName");
//                    img = conversation.getLatestMessageFromOthers().getStringAttribute("userHeadImage");

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e("toChatUsername", nickname + "  " + img);
                }
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);

                    // it's single chat
                    intent.putExtra(HuanUtils.EXTRA_USER_ID,username );
                    intent.putExtra("userNickName", nickname);
                    intent.putExtra("userHeadImage", img);
                    //这个人和你聊天的最后一条消息里面看是谁发的。判断方法就是取出这条消息发的人的环信id与你自己本地存储的环信id做匹配。是你发的与不是你发的，传入的值正好相反。
//                    if (!conversation.getLastMessage().getFrom().equals("SELL" + utils.getUserid())) {
//                        intent.putExtra("to_headportrait", conversation.getLastMessage().getStringAttribute("from_headportrait", ""));
//                        intent.putExtra("to_username", conversation.getLastMessage().getStringAttribute("from_username", ""));
//                        intent.putExtra("to_user_id", conversation.getLastMessage().getStringAttribute("from_user_id", ""));
//                    } else {
//                        intent.putExtra("to_headportrait", conversation.getLastMessage().getStringAttribute("to_headportrait", ""));
//                        intent.putExtra("to_username", conversation.getLastMessage().getStringAttribute("to_username", ""));
//                        intent.putExtra("to_user_id", conversation.getLastMessage().getStringAttribute("to_user_id", ""));
//                    }

                    startActivity(intent);
                }
            }
        });

        super.setUpView();
        //end of red packet code
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
        //( (MyMessageActivity)getActivity()).updateUnreadLabel();
        return true;
    }

}
