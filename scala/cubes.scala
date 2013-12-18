import scala.util.control.Breaks._

//Usage from cli: $ scala cubes.scala

//will print out smallest cube root whose cube contains permutations of at least 5 total cubes. 

object Cubes extends App {
	override def main(args: Array[String]) {
		var n = 0
		var targetPerms = 5
		
		val map = scala.collection.mutable.Map[String,List[Map[String,String]]]() // keep a list of permutations per cube
		var magicCubeRoot = -1;

		val cubeInt = (x:Long) => (x * x * x)
		// calc long^3 -> calc largest permutation -> store cube into largest perm list
		// stop when we add a 5th item to any list

		def longestPermString(x:String):String = {
			val strList = x.toList
			val sortedList = strList.sorted(Ordering[Char].reverse)
			val sortedStr = sortedList.mkString
			return sortedStr
		}

		breakable {
			while(true) {
				val cube = cubeInt(n)
				val cubeString = cube.toString()
				val mapKey = longestPermString(cubeString)
	
				if(map.contains(mapKey)) {
					//already has some perms inside
					val permList = map(mapKey)
					//println("Existing List: " + permList)
					val newList = permList ::: List(Map(n.toString() -> cubeString))
					//println("New List: " + newList)
					map(mapKey) = newList
		
					if (newList.size >= targetPerms) {
						magicCubeRoot = n;
						break;
					}
				} else {
					map(mapKey) = List(Map(n.toString() -> cubeString))
				}
	
				n = n + 1
			}
		}

		if (magicCubeRoot > 0)
		{
			val cube = cubeInt(magicCubeRoot)
			val cubeString = cube.toString()
			val mapKey = longestPermString(cubeString)
			val cubeLists = map(mapKey)
			val smallestCube = cubeLists.head
			println("Smallest cube with 5 permutations of other cubes is: "+smallestCube)
			println("Full List: " + cubeLists)
		} else {
			println ("Finished running without finding cubes with 5 permutations, uh oh!")
		}
	}
}