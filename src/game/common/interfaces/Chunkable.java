package game.common.interfaces;

import game.core.engine.camera.AdvCamera2D;
import game.core.engine.position.Position;
import game.exceptions.ChunkGenerationException;

/**
 * Interface for objects that support chunk generation.
 *
 * @param <T> The type of the chunk
 * @author FaintShadow
 */
public interface Chunkable<T>{
    /**
     * Generates all chunks <i>(when not using the camera)</i>.
     *
     * @throws ChunkGenerationException If an error occurs during chunk generation
     */
    default void generateChunks() throws ChunkGenerationException{}

    /**
     * Generates all chunks <i>(when using the camera)</i>.
     *
     * @param advCamera2D The camera to use for chunk generation
     * @throws ChunkGenerationException If an error occurs during chunk generation
     */
    default void generateChunks(AdvCamera2D advCamera2D) throws ChunkGenerationException{}

    /**
     * Generates a single chunk.
     *
     * @param position The position of the chunk
     * @return The generated chunk
     * @throws ChunkGenerationException If an error occurs during chunk generation
     */
    T generateChunk(Position position) throws ChunkGenerationException;
}
