package com.example.messagingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class TabAccessorAdapter extends FragmentPagerAdapter {


    public TabAccessorAdapter(FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new ChatsFragment();

            case 1:
                return new GroupsFragment();

            case 2:
                return new ContactsFragment();

            default:
                    return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);

        switch (position)
        {
            case 0:
               return "Chats";
            case 1:
                return "Groups";

            case 2:
                return "Contacts";

            default:
                return null;
        }
    }
}
