import scala.scalajs.js
import scala.scalajs.js.{Date, JSApp}
import scala.scalajs.js.annotation.JSExport
import js.Dynamic.{ global => g }
import org.scalajs.dom
import dom._


@JSExport
object Index {

  val W = 500
  val H = 500
  val R = 300.0
  val LIFE_MAX = 100
  val NUM = 50

  class Particle(ctx: CanvasRenderingContext2D, x: Double = 0, y: Double = 0) {
    var pX = x
    var pY = y

    var vX = Math.random() * 10 - 5
    var vY = Math.random() * 10 - 5

    var colR = Math.floor(Math.random() * 255)
    var colG = Math.floor(Math.random() * 255)
    var colB = Math.floor(Math.random() * 255)
    var colA = 1.0

    var startLife = Math.floor(LIFE_MAX * Math.random())
    var life = startLife
    var radius = R

    def initialize() = {
      this.pX = x
      this.pY = y
      this.vX = Math.random() * 10 - 5
      this.vY = Math.random() * 10 - 5
      this.colR = Math.floor(Math.random() * 255)
      this.colG = Math.floor(Math.random() * 255)
      this.colB = Math.floor(Math.random() * 255)
      this.colA = 1.0
      this.startLife = Math.floor(LIFE_MAX * Math.random())
      this.life = startLife
      this.radius = R
    }

    def draw(posX: Double, posY: Double): Unit = {
      ctx.beginPath()
      ctx.fillStyle = this.gradient()
      ctx.arc(this.pX, this.pY, radius, Math.PI * 2, 0)
      ctx.globalCompositeOperation = "lighter"
      ctx.fill()
      ctx.closePath()
    }

    def gradient() = {
      val r = if(this.radius.isNaN) R else this.radius
      val g = this.ctx.createRadialGradient(this.pX, this.pY, 0, this.pX, this.pY, r)
      g.addColorStop(0,   s"rgba($colR, $colG, $colB ,1)")
      g.addColorStop(0.5, s"rgba($colR, $colG, $colB ,0.4)")
      g.addColorStop(1,   s"rgba($colR, $colG, $colB ,0)")
      g
    }

    def render() = {
      this.updatePosition()

      this.wrapPosition()
      this.draw(this.pX, this.pY)
      this.updateParam()
    }

    def updatePosition() = {
      this.pX += vX
      this.pY += vY
    }

    def updateParam() = {
      val ratio = this.life / this.startLife
      this.colA -= ratio
      this.life -= 1
      this.radius = 30 / ratio
      if(this.radius > R) this.radius = R
      if(life == 0) this.initialize()
    }

    def wrapPosition() = {
      if(this.pX < 0) this.pX = W
      if(this.pX > W) this.pX = 0
      if(this.pY < 0) this.pY = H
      if(this.pY > H) this.pY = 0
    }
  }

  @JSExport
  def particle(canvas: html.Canvas) = {

    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    canvas.width = W
    canvas.height = H

    val particles = (0 to NUM).map{ _ =>
      val posX = Math.random() * 120
      val posY = Math.random() * 20
      new Particle(ctx, posX, posY)
    }.toArray

    render(0)

    def render(d: Double): Unit = {
      ctx.clearRect(0, 0, W, H)
      particles.foreach(_.render())
      window.requestAnimationFrame(render _)
    }
  }
}
