package com.hardtm.daggerpro.features.bash.models

import com.google.gson.annotations.SerializedName

data class BashDataModel(
    @SerializedName("site") val site: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("elementPureHtml") val elementPureHtml: String?
)