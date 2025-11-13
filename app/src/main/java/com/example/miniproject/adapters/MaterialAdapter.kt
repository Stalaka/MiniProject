package com.example.miniproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.miniproject.R
import com.example.miniproject.models.PassportMaterial

class MaterialAdapter(private var materials: List<PassportMaterial>) :
    RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    inner class MaterialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.material_name_text)
        val quantityTextView: TextView = itemView.findViewById(R.id.material_quantity_text)
        val statusTextView: TextView = itemView.findViewById(R.id.material_status_text)

        fun bind(material: PassportMaterial) {
            nameTextView.text = material.name
            quantityTextView.text = material.quantity.toString()
            statusTextView.text = "Status: ${material.status}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_material, parent, false)
        return MaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.bind(materials[position])
    }

    override fun getItemCount(): Int {
        return materials.size
    }

    fun updateData(newMaterials: List<PassportMaterial>) {
        materials = newMaterials
        notifyDataSetChanged()
    }
}