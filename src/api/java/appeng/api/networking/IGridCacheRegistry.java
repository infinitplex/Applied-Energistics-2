/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AlgorithmX2
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package appeng.api.networking;

import java.util.Map;

import javax.annotation.Nonnull;

/**
 * A registry of grid caches to extend grid functionality.
 */
public interface IGridCacheRegistry {

    /**
     * Register a new grid cache for use during operation, must be called during the
     * loading phase.
     *
     * @param iface   grid cache class
     * @param factory Factory for creating a new instance for each constructed grid
     */
    <T extends IGridCache> void registerGridCache(@Nonnull Class<T> iface, @Nonnull IGridCacheFactory<T> factory);

    /**
     * requests a new INSTANCE of a grid cache for use, used internally
     *
     * @param grid grid
     *
     * @return a new Map of IGridCaches from the registry, called from IGrid when
     *         constructing a new grid.
     */
    Map<Class<? extends IGridCache>, IGridCache> createCacheInstance(IGrid grid);
}
