package com.example.bbottodi.di

import android.content.Context

class ContainerWithContext(
    private val parentContainer: Container? = null,
    private val module: Module,
    private val context: Context?,
) : Container(parentContainer, module)
