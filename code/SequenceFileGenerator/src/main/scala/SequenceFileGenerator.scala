import java.io.File
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.SequenceFile
import org.apache.hadoop.io.Text

/**
 * Script to convert a nested structure of small files in to one Sequence File
 * ready to be processed by Hadoop
 */
object SequenceFileGenerator {

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
   * Write a File object to the specified SequenceFile Writer
   * @param file File to add to the SequenceFile
   * @param writer The writer to write to the SequenceFile
   * @return Updated SequenceWriter
   */
  def writeToSequenceFile(file: File, writer: SequenceFile.Writer): SequenceFile.Writer = {
    val key = new Text()
    key.set(file.getName)
    val value = new Text()
    value.set(scala.io.Source.fromFile(file).getLines().mkString)
    writer.append(key, value)
    writer
  }

  /**
   * Application entry point
   * @param args
   *  Input - Path to read files from
   *  Output - SequenceFile output path
   */
  def main(args: Array[String]) {
    val conf = new Configuration()
    val fs = FileSystem.getLocal(conf)
    val input = args(0)
    val output = args(1)

    val writer = SequenceFile.createWriter(fs,
      conf,
      new Path(output),
      classOf[Text],
      classOf[Text])

    // Close over the sequence writer, so we're writing to the
    // stream returned by the last writeToSequenceFile
    def write(tuple: (File, Int), total: Int) = {
      val f = tuple._1
      val i = tuple._2

      writeToSequenceFile(f, writer)

      val getPercent = (x: Int, y: Int) => (x / y) * 100
      val percent = getPercent(i, total)
      val lastPercent = getPercent(i-1, total)

      if (percent - lastPercent > 1) {
        System.out.println(percent.toString + "% complete")
      }
    }

    val files = getHtmlFiles(new File(input))
    files.zipWithIndex.foreach(write(_, files.length))
  }
}
