import Link from 'next/link';

export default function Home() {
  return (
    <div className="flex flex-col gap-16">
      {/* Hero Banner */}
      <section className="bg-gradient-to-r from-red-50 to-red-100 py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h1 className="text-4xl md:text-6xl font-bold text-red-600 mb-6">
              Give Blood. Save Lives.
            </h1>
            <p className="text-xl md:text-2xl text-gray-700 mb-8">
              Every drop counts. Be a hero today.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link
                href="/register"
                className="bg-red-600 text-white hover:bg-red-700 px-8 py-3 rounded-full font-semibold text-lg transition-colors"
              >
                Register Now
              </Link>
              <Link
                href="/find-blood"
                className="bg-white text-red-600 border-2 border-red-600 hover:bg-red-50 px-8 py-3 rounded-full font-semibold text-lg transition-colors"
              >
                Find Blood
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl md:text-4xl font-bold text-center text-gray-900 mb-12">
            How It Works
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            {[
              {
                title: 'Register',
                description: 'Create your account in minutes',
                icon: '📝',
              },
              {
                title: 'Schedule',
                description: 'Book your donation appointment',
                icon: '📅',
              },
              {
                title: 'Donate',
                description: 'Give blood at your chosen center',
                icon: '❤️',
              },
              {
                title: 'Track',
                description: 'Monitor your donation history',
                icon: '📊',
              },
            ].map((step, index) => (
              <div key={index} className="text-center p-6 rounded-lg bg-white shadow-lg">
                <div className="text-4xl mb-4">{step.icon}</div>
                <h3 className="text-xl font-semibold text-gray-900 mb-2">{step.title}</h3>
                <p className="text-gray-600">{step.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Quick Stats */}
      <section className="bg-red-600 py-16 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
            <div>
              <div className="text-4xl font-bold mb-2">3,000+</div>
              <div className="text-lg">Units Donated</div>
            </div>
            <div>
              <div className="text-4xl font-bold mb-2">1,200+</div>
              <div className="text-lg">Registered Donors</div>
            </div>
            <div>
              <div className="text-4xl font-bold mb-2">500+</div>
              <div className="text-lg">Lives Saved</div>
            </div>
          </div>
        </div>
      </section>

      {/* Why Donate */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl md:text-4xl font-bold text-center text-gray-900 mb-12">
            Why Donate Blood?
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {[
              {
                title: 'Save Lives',
                description: 'One donation can save up to three lives',
              },
              {
                title: 'Always Needed',
                description: 'Someone needs blood every two seconds',
              },
              {
                title: 'Quick & Easy',
                description: 'The donation process takes only 30-45 minutes',
              },
              {
                title: 'Health Benefits',
                description: 'Regular donation can improve your health',
              },
              {
                title: 'Safe Process',
                description: 'Completely safe and medically supervised',
              },
              {
                title: 'Community Impact',
                description: 'Make a real difference in your community',
              },
            ].map((item, index) => (
              <div key={index} className="p-6 rounded-lg bg-gray-50 hover:bg-gray-100 transition-colors">
                <h3 className="text-xl font-semibold text-gray-900 mb-3">{item.title}</h3>
                <p className="text-gray-600">{item.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
