package com.ugleagle;

/**
 *
 * @author SURAJ
 */
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;

public class JavaNativeBridgeFfmTwo {

    public static void main(String[] args) throws Throwable {
        // Load the shared library
        System.loadLibrary("print_numbers"); // Looks for libprint_numbers.so or print_numbers.dll

        try (Arena arena = Arena.ofConfined()) {
            Linker linker = Linker.nativeLinker();
            SymbolLookup lookup = SymbolLookup.loaderLookup();

            MemorySegment symbol = lookup.find("print_numbers")
                    .orElseThrow(() -> new RuntimeException("Function not found"));

            // Define function signature: void print_numbers()
            FunctionDescriptor fd = FunctionDescriptor.ofVoid();

            MethodHandle mh = linker.downcallHandle(symbol, fd);

            mh.invokeExact(); // Call the C function
        }
    }
}
