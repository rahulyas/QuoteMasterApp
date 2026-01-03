package com.quotemaster.quotemasterapp.utils

enum class CommonNamePrefix(val prefix: String) {
    MR("Mr"),
    MS("Ms"),
    MRS("Mrs"),
    DR("Dr"),
    SWAMI("Swami"),
    SIR("Sir"),
    PROF("Prof"),
    SAINT("Saint"),
    GURU("Guru"),
    LORD("Lord");

    companion object {
        fun matches(query: String): Boolean {
            val trimmedQuery = query.trim()
            return entries.any { trimmedQuery.startsWith(it.prefix, ignoreCase = true) }
        }
    }
}
