package no.hiof.bachelor.premacare.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label

data class BottomNavItems(val screen: AppScreens, val icon: ImageVector, val label: String)
