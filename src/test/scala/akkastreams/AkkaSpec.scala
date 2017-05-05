package akkastreams

//class AkkaSpec extends WordSpec {
//
//  "flow" should {
//
//    "abc" in {
//      println("a "+await(createGraph(CheckId("a"))))
//    }
//
//  }
//
//  def createGraph(checkId: CheckId): Future[String] = {
//
//    val src: Source[String, NotUsed] = Source.single(checkId.value)
//
//    val out: Sink[Seq[String], Future[String]] = Sink.fold[String, Seq[String]]("") { (acc, line) =>
//      line.foreach(println)
//
//      println(s"acc: $acc")
//
//      line.mkString(", ")
//    }
//
//    val graph: RunnableGraph[(NotUsed, Future[String])] =
//      RunnableGraph.fromGraph(GraphDSL.create(src, out)((_, _)) { implicit builder =>
//        (source, dest) =>
//
//
//
//          val broadcaster = builder.add(Broadcast[String](3))
//
//          val files = Flow[String].map(checkId => Seq(s"$checkId file1", s"$checkId file2"))
//          val docs = Flow[String].map(checkId => Seq(s"$checkId docs1", s"$checkId docs2"))
//          val contents = Flow[String].map(checkId => Seq(s"$checkId conts1", s"$checkId conts2"))
//
//          val zipper = builder.add(ZipWith[Seq[String], Seq[String], Seq[String], Seq[(String, String, String)]] {
//            case (fls, dcs, cnts) => fls.zip(dcs).zip(cnts).map {
//              case ((file, doc), content) => (file, doc, content)
//            }
//          })
//
//          val sender: Flow[Seq[(String, String, String)], Seq[String], NotUsed] =
//            Flow[Seq[(String, String, String)]].map(_.map {
//              case (file, doc, content) =>
//                s"$file -> $doc -> $content"
//            })
//
//          val sendingMarker: Flow[Seq[String], Seq[String], NotUsed] =
//            Flow[Seq[String]].map(_.map {
//              case (previousResults) =>
//                s"$previousResults | marked"
//            })
//
//          val auditer: Flow[Seq[String], Seq[String], NotUsed] =
//            Flow[Seq[String]].map(_.map {
//              case (previousResults) =>
//                s"$previousResults | audited"
//            })
//
//          val logger: Flow[Seq[String], Seq[String], NotUsed] =
//            Flow[Seq[String]].map(_.map {
//              case (previousResults) =>
//                s"$previousResults | logged"
//            })
//
//          source ~> broadcaster
//
//          broadcaster ~> files ~> zipper.in0
//          broadcaster ~> docs ~> zipper.in1
//          broadcaster ~> contents ~> zipper.in2
//
//          zipper.out ~> sender ~> sendingMarker ~> auditer ~> logger ~> dest
//
//          ClosedShape
//      })
//
//    val (_, result) = graph.run()
//    result
//  }
//}
