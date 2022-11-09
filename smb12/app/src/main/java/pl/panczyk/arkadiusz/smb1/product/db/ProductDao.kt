package pl.panczyk.arkadiusz.smb1.product.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getProducts(): LiveData<List<Product>>

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Query("DELETE FROM product WHERE product.id = :id")
    fun delete(id: Long)

    @Query("DELETE FROM product")
    fun deleteAll()

}