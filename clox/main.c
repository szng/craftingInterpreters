#include "common.h"
#include "chunk.h"
#include "debug.h"
#include "vm.h"

int main(int argc, const char *argv[])
{
    initVM();

    Chunk chunk;
    initChunk(&chunk);

    writeConstant(&chunk, 42, 1);
    writeConstant(&chunk, 2, 1);

    writeChunk(&chunk, OP_ADD, 1);

    writeConstant(&chunk, 11, 1);
    writeChunk(&chunk, OP_DIVIDE, 2);
    writeChunk(&chunk, OP_NEGATE, 2);
    writeChunk(&chunk, OP_RETURN, 2);

    disassembleChunk(&chunk, "test chunk");
    interpret(&chunk);
    freeVM();
    freeChunk(&chunk);
    return 0;
}