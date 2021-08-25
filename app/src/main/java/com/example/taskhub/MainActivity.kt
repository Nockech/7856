package com.example.taskhub

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.taskhub.utils.NotifyService
import com.example.taskhub.utils.TimeManager
import com.google.android.material.navigation.NavigationView
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var todayTasksFragment : Fragment
    lateinit var addTaskFragment : Fragment
    lateinit var plannedTasksFragment: Fragment

    //private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todayTasksFragment = TodayTasksFragment()
        addTaskFragment = AddTaskFragment()
        plannedTasksFragment = PlannedTasksFragment()

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigation: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigation.getHeaderView(0)

        initDrawer(drawer, navigation)
        NotifyService.initNotifyService(applicationContext)
        setLocale()

        navHeaderTextSet(headerView)
    }


    private fun initDrawer(drawer: DrawerLayout, nav: NavigationView) {
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setCurrentFragment(todayTasksFragment)

        nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item1 -> {
                    this.title = "Today tasks"
                    setCurrentFragment(todayTasksFragment)
                }
                R.id.item2 -> {
                    this.title = "Planned tasks"
                    setCurrentFragment(plannedTasksFragment)
                }
                R.id.item3 -> {
                    this.title = "Add task"
                    setCurrentFragment(addTaskFragment)
                }
            }
            drawer.closeDrawers()
            true
        }

    }

    private fun navHeaderTextSet(headerView: View){
        //Today date set
        val menuDate = headerView.findViewById<TextView>(R.id.menu_date_text)
        menuDate.text = TimeManager.getNowDate()

        //Today day of week set
        val menuWeek = headerView.findViewById<TextView>(R.id.menu_week_date)
        menuWeek.text = TimeManager.getNowWeekDay()
    }

    private fun setLocale(){
        val locale = Locale.Builder()
            .setRegion("GB")
            .setLanguage("US")
            .build()

        Locale.setDefault(locale)

        resources.configuration.setLocale(locale)
    }

    /*
    fun refreshCurrentFragment(){
        setCurrentFragment(currentFragment)
    }*/
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            //currentFragment = fragment
            replace(R.id.fl_fragment, fragment); commit()
        }
    }

    //Drawer open handle
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
