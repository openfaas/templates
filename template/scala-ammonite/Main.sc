//Main.sc
import $file.function.Function

@main
def read(): Unit = {
        for (ln <- io.Source.stdin.getLines) Function.handle(ln)
}