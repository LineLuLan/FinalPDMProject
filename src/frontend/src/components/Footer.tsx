export const Footer = () => {
  return (
    <footer className="bg-white shadow-lg mt-auto">
      <div className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col md:flex-row justify-between items-center">
          <div className="mb-4 md:mb-0">
            <span className="text-primary font-bold text-xl">Healthcare</span>
            <p className="text-gray-600 mt-2">Saving lives through blood donation</p>
          </div>
          
          <div className="flex flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-8">
            <div>
              <h3 className="font-semibold text-gray-800 mb-2">Quick Links</h3>
              <ul className="space-y-2">
                <li>
                  <a href="/" className="text-gray-600 hover:text-primary">Home</a>
                </li>
                <li>
                  <a href="/about" className="text-gray-600 hover:text-primary">About Us</a>
                </li>
                <li>
                  <a href="/donate-blood" className="text-gray-600 hover:text-primary">Register to Donate Blood</a>
                </li>
              </ul>
            </div>
            
            <div>
              <h3 className="font-semibold text-gray-800 mb-2">Contact</h3>
              <ul className="space-y-2">
                <li className="text-gray-600">
                  <span className="font-medium">Email:</span> info@healthcare.com
                </li>
                <li className="text-gray-600">
                  <span className="font-medium">Phone:</span> +1 234 567 890
                </li>
              </ul>
            </div>
          </div>
        </div>
        
        <div className="border-t border-gray-200 mt-8 pt-6 text-center">
          <p className="text-gray-600">
            © {new Date().getFullYear()} Healthcare. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  )
}