package com.example.threads.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threads.model.ThreadModel
import com.example.threads.model.UserModel
import com.example.threads.utils.SharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class UserViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val threadRef = db.getReference("threads")
    val userRef = db.getReference("users")


    private val _threads = MutableLiveData(listOf<ThreadModel>())
    val thread : LiveData<List<ThreadModel>> get() = _threads

    private val  _followerList= MutableLiveData(listOf<String>())
    val followerList : LiveData<List<String>> get() = _followerList

    private val  _followingList= MutableLiveData(listOf<String>())
    val followingList : LiveData<List<String>> get() = _followingList

    private val _users = MutableLiveData(UserModel())
    val users : LiveData<UserModel> get() = _users

    fun fetchUser(uid:String){
        userRef.child(uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _users.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

    fun fetchThreads(uid:String){
        threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList = snapshot.children.mapNotNull {
                    it.getValue(ThreadModel::class.java)
                }
                _threads.postValue(threadList)
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

    val firestoreDb = Firebase.firestore

    fun followUsers(userId:String, currentUserId:String){

        val ref = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        ref.update("followingIds", FieldValue.arrayUnion(userId))
        followerRef.update("followerIds", FieldValue.arrayUnion(currentUserId))
    }
    fun getFollowers(userId:String){

        firestoreDb.collection("followers").document(userId)
            .addSnapshotListener{ value, error ->

                val followerIds = value?.get("followerIds") as? List<String> ?: listOf()
                _followerList.postValue(followerIds)
            }
    }
    fun getFollowing(userId:String){

        firestoreDb.collection("following").document(userId)
            .addSnapshotListener{ value, error ->

                val followerIds = value?.get("followingIds") as? List<String> ?: listOf()
                _followingList.postValue(followerIds)
            }
    }

}