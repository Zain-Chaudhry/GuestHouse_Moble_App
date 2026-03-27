package com.example.diamondguesthouse.appNavigation

import androidx.compose.runtime.snapshots.SnapshotStateList

sealed interface NavCommand {
    data class Push(val key: GuestHouseNavKey) : NavCommand
    data object Pop : NavCommand
    data class ReplaceRoot(val key: GuestHouseNavKey) : NavCommand
}

fun SnapshotStateList<GuestHouseNavKey>.applyNavCommand(cmd: NavCommand) {
    when (cmd) {
        is NavCommand.Push -> add(cmd.key)
        NavCommand.Pop -> removeLastOrNull()
        is NavCommand.ReplaceRoot -> {
            clear()
            add(cmd.key)
        }
    }
}

typealias OnGuestHouseNavigate = (NavCommand) -> Unit
