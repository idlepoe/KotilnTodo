package lee.study.grocerylist

import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_todolist.*
import kotlinx.android.synthetic.main.item.view.*
import android.provider.Settings.Secure
import com.google.firebase.database.FirebaseDatabase
import java.security.AccessController.getContext

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "Main"
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todolist)

        todolist_recyclerview_todolist.adapter = adapter


        add_button_todolist.setOnClickListener {

            val title = itemname_edittext_todolist.text.toString()
            if (title.isNotEmpty()) {

                Log.d(TAG, "attempt itemname : $title")

                val todoList = TodoList(Todo(title, false, 1))

                todolist_recyclerview_todolist.scrollToPosition(adapter.itemCount - 1)
                adapter.add(todoList)
            }
        }

        delete_button_todolist.setOnClickListener {
            if (adapter.getItemCount() == 0) return@setOnClickListener
            adapter.removeGroup(adapter.getItemCount() - 1)
        }

        clear_button_todolist.setOnClickListener {
            val title = itemname_edittext_todolist.text.toString()
            Log.d(TAG, "attempt clear itemname : $title")

            itemname_edittext_todolist.text.clear()
        }

        adapter.setOnItemClickListener { item, view ->
            val index = adapter.getAdapterPosition(item)

            item as TodoList
            var todo = item.todo

            adapter.removeGroup(index)

            adapter.add(index, TodoList(Todo(todo.title, !todo.check, todo.timestamp)))

            adapter.notifyDataSetChanged()
        }

        adapter.setOnItemLongClickListener { item, view ->
            val index = adapter.getAdapterPosition(item)
            adapter.removeGroup(index)

            return@setOnItemLongClickListener true
        }


        todolist_recyclerview_todolist.adapter = adapter

        todolist_recyclerview_todolist.scrollToPosition(adapter.itemCount - 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}


class Todo(var title: String, val check: Boolean, val timestamp: Long) {
    constructor() : this("", false, -1)
}

class TodoList(val todo: Todo) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val red = 225 - (position * 25)
        val green = 0
        val blue = 0

        val text_red = 225 - red
        val text_green = 225 - green
        val text_blue = 225 - blue

        viewHolder.itemView.setBackgroundColor(Color.rgb(red, green, blue))
        viewHolder.itemView.check_checkbox_todolist.isChecked = todo.check
        viewHolder.itemView.strike_imageview_itemrow.alpha = if (todo.check) 1F else 0F
        viewHolder.itemView.item_textview_itemrow.text = todo.title
        viewHolder.itemView.item_textview_itemrow.setTextColor(
            Color.rgb(
                text_red,
                text_green,
                text_blue
            )
        )
        viewHolder.itemView.timestamp_textview_itemrow.text = ""
    }

    override fun getLayout(): Int {
        return R.layout.item
    }

}