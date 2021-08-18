package spiral.bit.dev.sunsetnotesapp.data.mappers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, R> Flow<List<T>>.mapItems(transform: (T) -> R) = map { list ->
    list.map {
        transform(it)
    }
}