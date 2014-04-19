import java.io.File
import com.github.tototoshi.csv._

/**
 * Script to convert a nested structure of small files in to one file
 * ready to be processed by Nephele (currently a CSV)
 */
object NepheleFileConvertor {

  /**
   * Recursively navigate a file system tree and flatten into a list of files
   * @param f File representing a directory
   * @return All files contained in the directory or any subdirectory
   */
  def getFileTree(f: File): Stream[File] = {
    f #::
      (if (f.isDirectory)
        f.listFiles().toStream.flatMap(getFileTree)
      else
        Stream.empty)
  }

  /**
   * Get a list of HTML files in a directory (recursively)
   * @param path Path to search for HTML files
   * @return A Stream of HTML files found in the file tree
   */
  def getHtmlFiles(path: File): Stream[File] = getFileTree(path).filter(f => f.getName.endsWith(".htm") || f.getName.endsWith(".html"))

  /**
   * Write a File object to the specified CSVWriter
   * @param file File to add to the CSV
   * @param writer The writer to write to the CSV
   */
  def writeToFile(file: File, writer: CSVWriter) = {
    val key = file.getName
    val fileHandle = scala.io.Source.fromFile(file)
    val value = fileHandle.getLines().mkString
    writer.writeRow(List(key, value))
    fileHandle.close()
    writer.flush()
  }

  /**
   * Measure the progress of a method (with 1 parameter)
   * @param tuple A tuple of (Method Parameter, Index of item)
   * @param total The total number of items in the list being processed
   * @param method The method to call/track progress of
   * @tparam T The type of the argument passed to method
   */
  def progress[T](tuple: (T, Int), total: Int, method: Function[T, Unit]) = {
    val f = tuple._1
    val i = tuple._2

    method(f)

    val getPercent = (x: Int, y: Int) => (x.toFloat / y) * 100
    val percent = getPercent(i, total)
    System.out.println(percent.toString + "% complete")
  }

  /**
   * Application entry point
   * @param args
   *  Input - Path to read files from
   *  Output - Output path
   */
  def main(args: Array[String]) {
    val input = args(0)
    val output = args(1)

    val writer = CSVWriter.open(output)

    val files = getHtmlFiles(new File(input))
    files.zipWithIndex.foreach(progress[File](_, files.length, writeToFile(_, writer)))
    writer.close()
  }
}