export default function About() {
  return (
    <div className="py-16">
      {/* Mission Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mb-16">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-12 items-center">
          <div className="order-2 md:order-1">
            <h1 className="text-4xl font-bold text-gray-900 mb-6">Our Mission</h1>
            <p className="text-lg text-gray-600 mb-6">
              Our mission is to bridge the gap between blood donors and those in need, creating a seamless and efficient platform that saves lives. We believe that everyone should have quick access to safe blood donations when they need it most.
            </p>
            <p className="text-lg text-gray-600">
              Through innovative technology and community engagement, we&apos;re making blood donation more accessible, efficient, and impactful than ever before.
            </p>
          </div>
          <div className="order-1 md:order-2 bg-red-100 rounded-lg p-8 aspect-square flex items-center justify-center">
            <div className="text-red-600 text-9xl">❤️</div>
          </div>
        </div>
      </section>

      {/* Vision Section */}
      <section className="bg-red-50 py-16 mb-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto">
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Our Vision</h2>
            <p className="text-lg text-gray-600 mb-8">
              We envision a world where no one has to wait for life-saving blood donations. A future where technology and human compassion work together to ensure that blood is always available to those who need it.
            </p>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
              <div className="bg-white p-6 rounded-lg shadow-lg">
                <div className="text-4xl mb-4">🌍</div>
                <h3 className="text-xl font-semibold mb-2">Global Reach</h3>
                <p className="text-gray-600">Connecting donors worldwide</p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-lg">
                <div className="text-4xl mb-4">⚡</div>
                <h3 className="text-xl font-semibold mb-2">Quick Response</h3>
                <p className="text-gray-600">Immediate matching system</p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-lg">
                <div className="text-4xl mb-4">🤝</div>
                <h3 className="text-xl font-semibold mb-2">Community First</h3>
                <p className="text-gray-600">Building local networks</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Team Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 className="text-3xl font-bold text-center text-gray-900 mb-12">Our Team</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {[
            {
              name: 'Dr. Sarah Johnson',
              role: 'Medical Director',
              image: '👩‍⚕️',
            },
            {
              name: 'Michael Chen',
              role: 'Technical Lead',
              image: '👨‍💻',
            },
            {
              name: 'Emma Williams',
              role: 'Community Manager',
              image: '👩‍💼',
            },
          ].map((member, index) => (
            <div key={index} className="text-center">
              <div className="w-32 h-32 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center">
                <span className="text-5xl">{member.image}</span>
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-1">{member.name}</h3>
              <p className="text-gray-600">{member.role}</p>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}