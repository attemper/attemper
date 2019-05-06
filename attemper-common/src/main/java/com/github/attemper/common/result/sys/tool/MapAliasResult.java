package com.github.attemper.common.result.sys.tool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * data structure like map
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapAliasResult<K, V> {
    protected K key;

    protected V value;

}
